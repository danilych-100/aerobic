import { Pipe, PipeTransform } from '@angular/core';
import { CommandMember } from './commandReg.component';
import { CATEGORIES } from './regions';

@Pipe({
    name: 'membersFilter',
    pure: false
})
export class MembersFilterPipe implements PipeTransform {
    transform(items: CommandMember[], category: string): any {
        if (!items || !category) {
            return items;
        }
        // filter items array, items which match and return true will be
        // kept, false will be filtered out
        return items.filter(item => {
            const age = new Date().getFullYear() - item.birthDate.getFullYear();
            if (category === '6—8') {
                return age >= 6 && age <= 8;
            }
            if (category === '9—11') {
                return age >= 9 && age <= 11;
            }
            if (category === '12—14') {
                return age >= 12 && age <= 14;
            }
            if (category === '15—17') {
                return age >= 15 && age <= 17;
            }
            if (category === '18+') {
                return age >= 18;
            }
        });
    }
}
